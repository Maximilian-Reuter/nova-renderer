//
// Created by David on 15-Apr-16.
//

#include "glfw_gl_window.h"
#include "../../utils/utils.h"

#define ELPP_THREAD_SAFE
#include <easylogging++.h>
#include "../../input/InputHandler.h"
#include "../nova_renderer.h"
namespace nova {
    void error_callback(int error, const char *description) {
        LOG(ERROR) << "Error " << error << ": " << description;
    }

    
    glfw_gl_window::glfw_gl_window() {
        initialize_logging();

        glfwSetErrorCallback(error_callback);

        if(!glfwInit()) {
            LOG(FATAL) << "Could not initialize GLFW";
        }
		init();
    }

    int glfw_gl_window::init() {

		nlohmann::json &config = nova_renderer::get_render_settings().get_options();

		float view_width = config["settings"]["viewWidth"];
		float view_height = config["settings"]["viewHeight"];

        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 5);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        window = glfwCreateWindow((int)view_width, (int)view_height, "Minecraft Nova Renderer", NULL, NULL);
        if(!window) {
            LOG(FATAL) << "Could not initialize window :(";
        }
        LOG(INFO) << "GLFW window created";

        //renderdoc_manager = std::make_unique<RenderDocManager>(window, "C:\\Program Files\\RenderDoc\\renderdoc.dll", "capture");
        LOG(INFO) << "Hooked into RenderDoc";

        glfwMakeContextCurrent(window);
        gladLoadGLLoader((GLADloadproc) glfwGetProcAddress);

        if(!gladLoadGL()) {
            LOG(FATAL) << "Could not load OpenGL";
            return -1;
        }

        const GLubyte *vendor = glGetString(GL_VENDOR);
        LOG(INFO) << "Vendor: " << vendor;

        glfwGetFramebufferSize(window, &window_dimensions.x, &window_dimensions.y);
        glViewport(0, 0, window_dimensions.x, window_dimensions.y);

        glfwSetKeyCallback(window, key_callback);
		glfwSetCharCallback(window, key_character_callback);
		glfwSetMouseButtonCallback(window, mouse_button_callback);
		glfwSetCursorPosCallback(window, mouse_position_callback);
		glfwSwapInterval(0);
		
		return 0;
    }

    glfw_gl_window::~glfw_gl_window() {
        glfwTerminate();
    }

    void glfw_gl_window::destroy() {
        glfwDestroyWindow(window);
        window = NULL;
    }

    void glfw_gl_window::set_fullscreen(bool fullscreen) {
        // TODO: RAVEN WRIT THIS
    }

    bool glfw_gl_window::should_close() {
        return (bool) glfwWindowShouldClose(window);
    }

    glm::vec2 glfw_gl_window::get_size() {
        return window_dimensions;
    }

    void glfw_gl_window::end_frame() {
        // We're in thread 29
        glfwSwapBuffers(window);
        glfwPollEvents();

        glm::ivec2 new_window_size;
        glfwGetFramebufferSize(window, &new_window_size.x, &new_window_size.y);
		
        if(new_window_size != window_dimensions) {
            set_framebuffer_size(new_window_size);
        }
    }

    void glfw_gl_window::set_framebuffer_size(glm::ivec2 new_framebuffer_size) {
        window_dimensions = new_framebuffer_size;
        glViewport(0, 0, window_dimensions.x, window_dimensions.y);
    }

    void glfw_gl_window::on_config_change(nlohmann::json &new_config) {
        LOG(INFO) << "gl_glfw_window received the updated config";
        glfwSetWindowSize(window, new_config["viewWidth"], new_config["viewHeight"]);
    }

    void glfw_gl_window::on_config_loaded(nlohmann::json &config) {
		//glfwSetWindowSize(window, config["viewWidth"], config["viewHeight"]);
    }
}
