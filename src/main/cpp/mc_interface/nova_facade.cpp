/*!
 * \brief Provides implementations for all the functions that this library provides
 *
 * I'd like for the functions in this file to be a facade sort of thing, but idk if that will happen. These functions
 * look a lot like simple passthroughs.
 *
 * Although, many of them do things to convert from C-style things to C++-style things, so that's cool I guess
 *
 * The function in this file simply set data. Data is copied out of Minecraft objects. This might be a bit slow, but I
 * don't want to save a pointer that gets re-allocated by the JVM. I don't trust it enough (although maybe I should?)
 *
 * \author David
 */

#include "nova.h"
#include "../utils/export.h"
#include "../render/nova_renderer.h"
#include "../render/objects/textures/texture_manager.h"
#include "../data_loading/settings.h"

using namespace nova;

#define TEXTURE_MANAGER nova_renderer::instance->get_texture_manager()

// runs in thread 5

NOVA_API void initialize() {
    nova_renderer::init_instance();
}

NOVA_API void add_texture(mc_atlas_texture & texture, int atlas_type, int texture_type) {
    TEXTURE_MANAGER.add_texture(
            texture,
            static_cast<texture_manager::atlas_type>(atlas_type),
            static_cast<texture_manager::texture_type >(texture_type)
    );
}

NOVA_API void reset_texture_manager() {
    TEXTURE_MANAGER.reset();
}

NOVA_API void add_texture_location(mc_texture_atlas_location location) {
    TEXTURE_MANAGER.add_texture_location(location);
}

NOVA_API int get_max_texture_size() {
    return 8096;//TEXTURE_MANAGER.get_max_texture_size();
}

NOVA_API void execute_frame() {
    nova_renderer::instance->render_frame();
}

NOVA_API bool should_close() {
    return nova_renderer::instance->should_end();
}

NOVA_API void send_change_gui_screen_command(mc_set_gui_screen_command * set_gui_screen) {
    nova_renderer::instance->get_mesh_builder().build_geometry(set_gui_screen->screen);
}

NOVA_API void set_string_setting(const char * setting_name, const char * setting_value) {
    settings& settings = nova_renderer::instance->get_render_settings();
    settings.get_options()[setting_name] = setting_value;
    settings.update_config_changed();
}
